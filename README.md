kvstore
=======

KVStore - Sample application using CKite

## Running a 3 member KVStore cluster on top of CKite

#### Run Member 1 boostrapping a new cluster just the first time. Take note that you have to bootstrap a new cluster only for the very first node. Subsequent runs of Member 1 starts normal (without bootstrap).
```bash
sbt run -Dckite.finagle.listen-address=localhost:9091 -Dckite.datadir=/tmp/ckite/member1 -Dckite.bootstrap=true


23:18:38.002 INFO  [run-main] ckite.RLog - Initializing...
23:18:38.054 INFO  [run-main] ckite.RLog - No Snapshot was found
23:18:38.061 INFO  [CommandApplier-worker-1] ckite.rlog.CommandApplier - Starting applier from index #0
23:18:38.108 INFO  [run-main] ckite.rlog.CommandApplier - No entry to replay. commitIndex is #0
Jun 10, 2014 11:18:39 PM com.twitter.finagle.Init$ apply
INFO: Finagle version 6.6.2 (rev=2c5f728dcbd9d460a0e983aec6ae83e9bc75aa12) built at 20130916-180323
23:18:39.834 INFO  [run-main] ckite.Cluster - Starting CKite...
23:18:39.834 INFO  [run-main] ckite.Cluster - Bootstrapping a new Cluster...
23:18:39.837 DEBUG [run-main] ckite.LocalMember - Transition from Starter to Follower[0]
23:18:41.656 DEBUG [ElectionTimeout-worker-1] ckite.states.ElectionTimeout - Timeout reached! Time to elect a new leader
23:18:41.660 DEBUG [ElectionTimeout-worker-1] ckite.LocalMember - Transition from Follower[0] to Candidate[1]
23:18:41.795 DEBUG [ElectionTimeout-worker-1] ckite.states.Candidate - Start election
23:18:41.797 DEBUG [CandidateElection-worker-1] ckite.states.Election - Got 1 votes in a majority of 1
23:18:41.803 DEBUG [CandidateElection-worker-1] ckite.LocalMember - Transition from Candidate[1] to Leader[1]
23:18:41.930 DEBUG [CandidateElection-worker-1] ckite.states.Heartbeater - Start Heartbeater
23:18:41.937 INFO  [CandidateElection-worker-1] ckite.states.Leader - Will set a configuration with just myself: localhost:9091
23:18:42.015 INFO  [CandidateElection-worker-1] ckite.states.Leader - Start being Leader[1]
23:18:42.718 DEBUG [LogAppender-worker-1] ckite.Cluster - Applying NewConfiguration(List(localhost:9091))
23:18:42.741 INFO  [LogAppender-worker-1] ckite.Cluster - Cluster Configuration changed to (localhost:9091)
23:18:42.953 DEBUG [ForkJoinPool-1-worker-1] ckite.states.Leader - No member to replicate
23:18:43.012 DEBUG [CommandApplier-worker-1] ckite.rlog.CommandApplier - New commitIndex is #1
23:18:43.013 DEBUG [CommandApplier-worker-1] ckite.rlog.CommandApplier - Will apply committed entry LogEntry(term=1,index=1,NewConfiguration(List(localhost:9091)))
23:18:43.013 DEBUG [CommandApplier-worker-1] ckite.rlog.CommandApplier - Last applied index is #1

```

#### Run Member 2 pointing to Member 1 to join the cluster
```bash
sbt run -Dckite.finagle.listen-address=localhost:9092 -Dckite.datadir=/tmp/ckite/member2 -Dckite.members.0=localhost:9091


23:20:19.667 INFO  [run-main] ckite.RLog - Initializing...
23:20:19.787 INFO  [run-main] ckite.RLog - No Snapshot was found
23:20:19.791 INFO  [CommandApplier-worker-1] ckite.rlog.CommandApplier - Starting applier from index #0
23:20:19.876 INFO  [run-main] ckite.rlog.CommandApplier - No entry to replay. commitIndex is #0
Jun 10, 2014 11:20:20 PM com.twitter.finagle.Init$ apply
INFO: Finagle version 6.6.2 (rev=2c5f728dcbd9d460a0e983aec6ae83e9bc75aa12) built at 20130916-180323
23:20:21.554 INFO  [run-main] ckite.Cluster - Starting CKite...
23:20:21.555 INFO  [run-main] ckite.Cluster - Empty log & no snapshot
23:20:21.557 INFO  [run-main] ckite.Cluster - Will try to join an existing Cluster using the seeds: Buffer(localhost:9091)
23:20:21.561 DEBUG [run-main] ckite.LocalMember - Transition from Starter to Follower[0]
23:20:21.782 INFO  [run-main] ckite.Cluster - Try to join with localhost:9091
23:20:21.789 DEBUG [run-main] ckite.RemoteMember - Creating RemoteMember client for localhost:9091
23:20:24.408 DEBUG [run-main] ckite.RemoteMember - Joining with localhost:9091
23:20:26.760 DEBUG [Thrift-worker-1] ckite.states.Follower - localhost:9092 Step down from being Follower[0]
23:20:26.772 DEBUG [Thrift-worker-2] ckite.states.Follower - localhost:9092 Step down from being Follower[0]
23:20:26.777 DEBUG [Thrift-worker-2] ckite.LocalMember - Transition from Follower[0] to Follower[1]
23:20:26.814 DEBUG [Thrift-worker-4] ckite.states.Follower - localhost:9092 Step down from being Follower[0]
23:20:27.306 DEBUG [LogAppender-worker-1] ckite.Cluster - Applying NewConfiguration(List(localhost:9091))
23:20:27.315 DEBUG [LogAppender-worker-1] ckite.RemoteMember - Creating RemoteMember client for localhost:9091
23:20:27.324 INFO  [LogAppender-worker-1] ckite.Cluster - Cluster Configuration changed to (localhost:9091)
23:20:27.342 INFO  [Thrift-worker-5] ckite.states.Follower - Following Some(localhost:9091) in term[1]
23:20:27.569 DEBUG [CommandApplier-worker-1] ckite.rlog.CommandApplier - New commitIndex is #1
23:20:27.569 DEBUG [CommandApplier-worker-1] ckite.rlog.CommandApplier - Will apply committed entry LogEntry(term=1,index=1,NewConfiguration(List(localhost:9091)))
23:20:27.573 DEBUG [CommandApplier-worker-1] ckite.rlog.CommandApplier - Last applied index is #1
23:20:27.763 DEBUG [LogAppender-worker-1] ckite.Cluster - Applying JointConfiguration(List(localhost:9091),List(localhost:9091, localhost:9092))
23:20:27.778 INFO  [LogAppender-worker-1] ckite.Cluster - Cluster Configuration changed to [Cold=(localhost:9091), Cnew=(localhost:9091,localhost:9092)]
23:20:27.974 DEBUG [CommandApplier-worker-1] ckite.rlog.CommandApplier - New commitIndex is #2
23:20:27.975 DEBUG [CommandApplier-worker-1] ckite.rlog.CommandApplier - Will apply committed entry LogEntry(term=1,index=2,JointConfiguration(List(localhost:9091),List(localhost:9091, localhost:9092)))
23:20:28.000 DEBUG [CommandApplier-worker-1] ckite.rlog.CommandApplier - Last applied index is #2
23:20:28.106 INFO  [run-main] ckite.Cluster - Join successful
23:20:28.239 DEBUG [LogAppender-worker-1] ckite.Cluster - Applying NewConfiguration(List(localhost:9091, localhost:9092))
23:20:28.242 INFO  [LogAppender-worker-1] ckite.Cluster - Cluster Configuration changed to (localhost:9091,localhost:9092)
23:20:28.830 DEBUG [CommandApplier-worker-1] ckite.rlog.CommandApplier - New commitIndex is #3
23:20:28.831 DEBUG [CommandApplier-worker-1] ckite.rlog.CommandApplier - Will apply committed entry LogEntry(term=1,index=3,NewConfiguration(List(localhost:9091, localhost:9092)))
23:20:28.831 DEBUG [CommandApplier-worker-1] ckite.rlog.CommandApplier - Last applied index is #3
```
#### Run Member 3 pointing to Member 1 to join the cluster
```bash
sbt run -Dckite.finagle.listen-address=localhost:9093 -Dckite.datadir=/tmp/ckite/member3 -Dckite.members.0=localhost:9091
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

