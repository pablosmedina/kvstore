kvstore
=======

KVStore - Sample application using CKite

## Running a 3 member KVStore cluster on top of CKite

#### Run Member 1 boostrapping a new cluster just the first time. Take note that you have to bootstrap a new cluster only for the very first node. Subsequent runs of Member 1 starts normal (without bootstrap).
```bash
sbt run -Dckite.listen-address=localhost:9091 -Dckite.datadir=/tmp/ckite/member1 -Dckite.bootstrap=true
```
#### Run Member 2 pointing to Member 1 to join the cluster
```bash
sbt run -Dckite.listen-address=localhost:9092 -Dckite.datadir=/tmp/ckite/member2 -Dckite.members.0=localhost:9091
```
#### Run Member 3 pointing to Member 1 to join the cluster
```bash
sbt run -Dckite.listen-address=localhost:9093 -Dckite.datadir=/tmp/ckite/member3 -Dckite.members.0=localhost:9091
```
#### Put a key-value on the leader member (take a look at the logs for election result)
```bash
curl -X POST http://localhost:10091/kv/key1/value1
```
#### Get the value of key1. This is a consistent read. If the receiving node is not a Leader then it is forwarded to the Leader.
```bash
curl http://localhost:10092/kv/key1
```
#### Get the value of key1 performing a local read in member 2. This allows possible stale values.
```bash
curl http://localhost:10092/kv/key1?local=true
```
#### Checkout the admin console on any member to see the cluster status
```bash
curl http://localhost:10093/status
```


## Rest admin console

KVStore exposes an admin console showing its status and useful metrics. If the rpc port is 9091 then the admin console is exposed under http://localhost:10091/status by default.

#### Leader

```yaml
{
	cluster: {
		term: 1,
		state: "Leader",
		stateInfo: {
			leaderUptime: "13.hours+4.minutes+47.seconds+214.milliseconds",
			followers: {
				localhost:9093: {
					lastHeartbeatACK: "11.milliseconds",
					nextIndex: 10
				},
				localhost:9092: {
					lastHeartbeatACK: "8.milliseconds",
					nextIndex: 10
				}
				localhost:9095: {
					lastHeartbeatACK: "10.milliseconds",
					nextIndex: 10
				}
				localhost:9094: {
					lastHeartbeatACK: "12.milliseconds",
					nextIndex: 10
				}
			}
		}
	},
	log: {
		length: 9,
		commitIndex: 9,
		lastEntry: {
			term: 1,
			index: 9,
			command: {
				key: "foo",
				value: "bar"
			}
		}
	}
}
```

#### Follower
```yaml
{
	cluster: {
		term: 1,
		state: "Follower",
		stateInfo: {
			following: "Some(localhost:9091)"
		}
	},
	log: {
		length: 9,
		commitIndex: 9,
		lastEntry: {
			term: 1,
			index: 9,
			command: {
				key: "foo",
				value: "bar"
			}
		}
	}
}
```

