package com.h3c.iot.wordcount;

import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;

public class ClusterDemo extends UntypedActor {
    Cluster cluster = Cluster.get(getContext().system());


    @Override
    public void preStart() throws Exception, Exception {
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(),
                ClusterEvent.UnreachableMember.class, ClusterEvent.MemberEvent.class);
        super.preStart();
    }

    @Override
    public void postStop() throws Exception, Exception {
        cluster.unsubscribe(getSelf());
        super.postStop();
    }

    public void onReceive(Object message) throws Exception, Exception {
        if (message instanceof ClusterEvent.MemberUp) {
            ClusterEvent.MemberUp mUp = (ClusterEvent.MemberUp) message;
            Member m = mUp.member();
            System.out.println(getSelf() + "-->Member " + m + "is Up: the role is " + m.roles());
        } else if (message instanceof ClusterEvent.UnreachableMember) {
            ClusterEvent.UnreachableMember mUnreachable = (ClusterEvent.UnreachableMember) message;
            Member m = mUnreachable.member();
            System.out.println(getSelf() + "-->Member " + m + "detected as unreachable: the role is  " + m.roles());
        } else if (message instanceof ClusterEvent.MemberRemoved) {
            ClusterEvent.MemberRemoved uRemoved = (ClusterEvent.MemberRemoved) message;
            Member m = uRemoved.member();
            System.out.println(getSelf() + "-->Member " + m + "is removed: the role is  " + m.roles());
        } else if (message instanceof ClusterEvent.MemberEvent) {
            ClusterEvent.MemberEvent me = (ClusterEvent.MemberEvent) message;
            Member m = me.member();
            System.out.println(getSelf() + "-->MemberEvent: " + m + " " + m.roles());
        } else {
            System.out.println(getSelf() + "-->other: " + message);
            unhandled(message);
        }
    }
}
