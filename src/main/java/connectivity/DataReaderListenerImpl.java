package connectivity;

import DDS.*;
import MissionData.AgentAction;
import MissionData.AgentActionDataReader;
import MissionData.AgentActionDataReaderHelper;
import MissionData.AgentActionHolder;
import interfaces.ISubscribedDataReceivedListener;

public class DataReaderListenerImpl extends _DataReaderListenerLocalBase {

//
//    public synchronized void on_data_available(DataReader reader) {
//        System.out.println("Inside the on_data_available method");
//
//        //Here Message is the name of struct defined in .idl file
//        AgentActionDataReader agentActionDataReader = AgentActionDataReaderHelper.narrow(reader);//MissionParameterDataReaderHelper.narrow(reader);
//        if (agentActionDataReader == null) {
//            System.err.println("ERROR: read: narrow failed.");
//            return;
//        }
//
//        AgentAction agentAction = new AgentAction();
//        agentAction.action_data = new String[0];
//
//        AgentActionHolder agentActionHolder = new AgentActionHolder(agentAction);
//        SampleInfoHolder sih = new SampleInfoHolder(new SampleInfo(0, 0, 0,
//                new Time_t(), 0, 0, 0, 0, 0, 0, 0, false, 0));
//        int status = agentActionDataReader.take_next_sample(agentActionHolder, sih);
//
//        if (status == RETCODE_OK.value) {
//
//            if (sih.value.valid_data) {
////            System.out.println("Setting the data");
////                if(agentActionHolder.value.from.toString().equalsIgnoreCase("GBRE")){
////                    WayPoint wayPoint=WayPoint.getInstance();
////                    System.out.println(agentActionHolder.value.count);
////                }
//                //listener.onSubscribedDataReceived(agentActionHolder.value.action_data);
//                for (String data : agentActionHolder.value.action_data){
//                    System.out.println(data);
//                }
//
//            }
//            else if (sih.value.instance_state ==
//                    NOT_ALIVE_DISPOSED_INSTANCE_STATE.value) {
//                System.out.println("instance is disposed");
//            }
//            else if (sih.value.instance_state ==
//                    NOT_ALIVE_NO_WRITERS_INSTANCE_STATE.value) {
//                System.out.println("instance is unregistered");
//            }
//            else {
//                System.out.println("DataReaderListenerImpl::on_data_available: "
//                        + "ERROR: received unknown instance state "
//                        + sih.value.instance_state);
//            }
//
//        } else if (status == RETCODE_NO_DATA.value) {
//            System.err.println("ERROR: reader received DDS::RETCODE_NO_DATA!");
//        } else {
//            System.err.println("ERROR: read Message: Error: " + status);
//        }
//    }
//
//    public void on_requested_deadline_missed(DataReader reader, RequestedDeadlineMissedStatus status) {
//        System.err.println("DataReaderListenerImpl.on_requested_deadline_missed");
//    }
//
//    public void on_requested_incompatible_qos(DataReader reader, RequestedIncompatibleQosStatus status) {
//        System.err.println("DataReaderListenerImpl.on_requested_incompatible_qos");
//    }
//
//    public void on_sample_rejected(DataReader reader, SampleRejectedStatus status) {
//        System.err.println("DataReaderListenerImpl.on_sample_rejected");
//    }
//
//    public void on_liveliness_changed(DataReader reader, LivelinessChangedStatus status) {
//        System.err.println("DataReaderListenerImpl.on_liveliness_changed");
//    }
//
//    public void on_subscription_matched(DataReader reader, SubscriptionMatchedStatus status) {
//        System.err.println("DataReaderListenerImpl.on_subscription_matched");
//    }
//
//    public void on_sample_lost(DataReader reader, SampleLostStatus status) {
//        System.err.println("DataReaderListenerImpl.on_sample_lost");
//    }

    private ISubscribedDataReceivedListener listener;

    public DataReaderListenerImpl( ISubscribedDataReceivedListener listener){
        this.listener = listener;
    }


    public synchronized void on_data_available(DataReader reader) {
        System.out.println("Inside the on_data_available method");

        //Here Message is the name of struct defined in .idl file
        AgentActionDataReader agentActionDataReader = AgentActionDataReaderHelper.narrow(reader);//MissionParameterDataReaderHelper.narrow(reader);
        if (agentActionDataReader == null) {
            System.err.println("ERROR: read: narrow failed.");
            return;
        }

        AgentAction agentAction = new AgentAction();
        agentAction.action_data = new String[0];

        AgentActionHolder agentActionHolder = new AgentActionHolder(agentAction);
        SampleInfoHolder sih = new SampleInfoHolder(new SampleInfo(0, 0, 0,
                new Time_t(), 0, 0, 0, 0, 0, 0, 0, false, 0));
        int status = agentActionDataReader.take_next_sample(agentActionHolder, sih);

        if (status == RETCODE_OK.value) {

            if (sih.value.valid_data) {
            System.out.println("Setting the data");
//                if(agentActionHolder.value.from.toString().equalsIgnoreCase("GBRE")){
//                    WayPoint wayPoint=WayPoint.getInstance();
//                    System.out.println(agentActionHolder.value.count);
//                }
                listener.onSubscribedDataReceived(agentActionHolder.value.action_data);

            }
            else if (sih.value.instance_state ==
                    NOT_ALIVE_DISPOSED_INSTANCE_STATE.value) {
                System.out.println("instance is disposed");
            }
            else if (sih.value.instance_state ==
                    NOT_ALIVE_NO_WRITERS_INSTANCE_STATE.value) {
                System.out.println("instance is unregistered");
            }
            else {
                System.out.println("DataReaderListenerImpl::on_data_available: "
                        + "ERROR: received unknown instance state "
                        + sih.value.instance_state);
            }

        } else if (status == RETCODE_NO_DATA.value) {
            System.err.println("ERROR: reader received DDS::RETCODE_NO_DATA!");
        } else {
            System.err.println("ERROR: read Message: Error: " + status);
        }
    }

    public void on_requested_deadline_missed(DataReader reader, RequestedDeadlineMissedStatus status) {
        System.err.println("DataReaderListenerImpl.on_requested_deadline_missed");
    }

    public void on_requested_incompatible_qos(DataReader reader, RequestedIncompatibleQosStatus status) {
        System.err.println("DataReaderListenerImpl.on_requested_incompatible_qos");
    }

    public void on_sample_rejected(DataReader reader, SampleRejectedStatus status) {
        System.err.println("DataReaderListenerImpl.on_sample_rejected");
    }

    public void on_liveliness_changed(DataReader reader, LivelinessChangedStatus status) {
        System.err.println("DataReaderListenerImpl.on_liveliness_changed");
    }

    public void on_subscription_matched(DataReader reader, SubscriptionMatchedStatus status) {
        System.err.println("DataReaderListenerImpl.on_subscription_matched");
    }

    public void on_sample_lost(DataReader reader, SampleLostStatus status) {
        System.err.println("DataReaderListenerImpl.on_sample_lost");
    }
}
