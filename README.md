kvstore
=======

KVStore - Sample application using CKite

## Running KVStore example (3 members)

#### Run Member 1
```bash
sbt run -Dport=9091 -Dmembers=localhost:9092,localhost:9093 -DdataDir=/tmp/ckite/member1
```
#### Run Member 2
```bash
sbt run -Dport=9092 -Dmembers=localhost:9091,localhost:9093 -DdataDir=/tmp/ckite/member2
```
#### Run Member 3
```bash
sbt run -Dport=9093 -Dmembers=localhost:9092,localhost:9091 -DdataDir=/tmp/ckite/member3
```
#### Put a key-value on the leader member (take a look at the logs for election result)
```bash
curl http://localhost:10091/put/key1/value1
```
#### Get the value of key1 replicated in member 2 
```bash
curl http://localhost:10092/get/key1
```
#### Checkout the admin console on any member to see the cluster status
```bash
curl http://localhost:10093/status
```
#### Add a new member (localhost:9094) to the Cluster
```bash
curl http://localhost:10091/changecluster/localhost:9091,localhost:9092,localhost:9093,localhost:9094
```
#### Run Member 4
```bash
sbt run -Dport=9094 -Dmembers=localhost:9092,localhost:9091,localhost:9093 -DdataDir=/tmp/ckite/member4
```

## Rest admin console

CKite exposes an admin console showing its status and useful metrics. If the rpc port is 9091 then the admin console is exposed under http://localhost:10091/status by default.

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

