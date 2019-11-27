package connectivity;

import DDS.*;
import MissionData.Telemetry;
import MissionData.TelemetryDataWriter;
import MissionData.TelemetryDataWriterHelper;
import MissionData.TelemetryTypeSupportImpl;
import OpenDDS.DCPS.DEFAULT_STATUS_MASK;
import OpenDDS.DCPS.TheParticipantFactory;
import OpenDDS.DCPS.TheServiceParticipant;
import org.omg.CORBA.StringSeqHolder;

public class Publish {

    private DomainParticipantFactory domainParticipantFactory;
    private DomainParticipant domainParticipant;
    private Topic topic;
    private Publisher publisher;
    private TelemetryTypeSupportImpl telemetryTypeSupport;
    private DataWriter dataWriter;
    private DataWriterQos dw_qos;
    private DataWriterQosHolder qosh;


    /*
    Initiate the object of domainParticipant Factory ,Domain Participant,topic,publisher,
    Default transport protocol
     */
    public void setUp(String args[]) {

        domainParticipantFactory = TheParticipantFactory.WithArgs(new StringSeqHolder(args));
        if (domainParticipantFactory == null) {
            System.err.println("ERROR: Domain Participant Factory not found");
            return;
        }

        //Create the domain Participant
        domainParticipant = domainParticipantFactory.create_participant(4,
                PARTICIPANT_QOS_DEFAULT.get(), null, DEFAULT_STATUS_MASK.value);
        if (domainParticipant == null) {
            System.err.println("ERROR: Domain Participant creation failed");
            return;
        }

        //Structure 1 Message
        telemetryTypeSupport = new TelemetryTypeSupportImpl();
        if (telemetryTypeSupport.register_type(domainParticipant, "") != RETCODE_OK.value) {
            System.err.println("ERROR: register_type failed");
            return;
        }

        //Topic1
        topic = domainParticipant.create_topic("Telemetry data",
                telemetryTypeSupport.get_type_name(),
                TOPIC_QOS_DEFAULT.get(),
                null,
                DEFAULT_STATUS_MASK.value);
        if (topic == null) {
            System.err.println("ERROR: Topic creation failed");
            return;
        }


        //Create Publisher
        publisher = domainParticipant.create_publisher(PUBLISHER_QOS_DEFAULT.get(), null,
                DEFAULT_STATUS_MASK.value);
        if (publisher == null) {
            System.err.println("ERROR: Publisher creation failed");
            return;
        }


        //Default Transport Protocol

        dw_qos = new DataWriterQos();
        dw_qos.durability = new DurabilityQosPolicy();
        dw_qos.durability.kind = DurabilityQosPolicyKind.from_int(0);
        dw_qos.durability_service = new DurabilityServiceQosPolicy();
        dw_qos.durability_service.history_kind = HistoryQosPolicyKind.from_int(0);
        dw_qos.durability_service.service_cleanup_delay = new Duration_t();
        dw_qos.deadline = new DeadlineQosPolicy();
        dw_qos.deadline.period = new Duration_t();
        dw_qos.latency_budget = new LatencyBudgetQosPolicy();
        dw_qos.latency_budget.duration = new Duration_t();
        dw_qos.liveliness = new LivelinessQosPolicy();
        dw_qos.liveliness.kind = LivelinessQosPolicyKind.from_int(0);
        dw_qos.liveliness.lease_duration = new Duration_t();
        dw_qos.reliability = new ReliabilityQosPolicy();
        dw_qos.reliability.kind = ReliabilityQosPolicyKind.from_int(0);
        dw_qos.reliability.max_blocking_time = new Duration_t();
        dw_qos.destination_order = new DestinationOrderQosPolicy();
        dw_qos.destination_order.kind = DestinationOrderQosPolicyKind.from_int(0);
        dw_qos.history = new HistoryQosPolicy();
        dw_qos.history.kind = HistoryQosPolicyKind.from_int(0);
        dw_qos.resource_limits = new ResourceLimitsQosPolicy();
        dw_qos.transport_priority = new TransportPriorityQosPolicy();
        dw_qos.lifespan = new LifespanQosPolicy();
        dw_qos.lifespan.duration = new Duration_t();
        dw_qos.user_data = new UserDataQosPolicy();
        dw_qos.user_data.value = new byte[0];
        dw_qos.ownership = new OwnershipQosPolicy();
        dw_qos.ownership.kind = OwnershipQosPolicyKind.from_int(0);
        dw_qos.ownership_strength = new OwnershipStrengthQosPolicy();
        dw_qos.writer_data_lifecycle = new WriterDataLifecycleQosPolicy();

        qosh = new DataWriterQosHolder(dw_qos);
        publisher.get_default_datawriter_qos(qosh);
        qosh.value.history.kind = HistoryQosPolicyKind.KEEP_ALL_HISTORY_QOS;

        //Initiate DataWriter
        dataWriter = publisher.create_datawriter(topic, qosh.value, null, DEFAULT_STATUS_MASK.value);
        if (dataWriter == null) {
            System.err.println("ERROR: DataWriter creation failed");
            return;
        }
        System.out.println("Publisher Created DataWriter Message data");

        StatusCondition sc = dataWriter.get_statuscondition();
        sc.set_enabled_statuses(PUBLICATION_MATCHED_STATUS.value);
        WaitSet ws = new WaitSet();
        ws.attach_condition(sc);
        PublicationMatchedStatusHolder matched =
                new PublicationMatchedStatusHolder(new PublicationMatchedStatus());
        Duration_t timeout = new Duration_t(DURATION_INFINITE_SEC.value,
                DURATION_INFINITE_NSEC.value);

        while (true) {
            System.out.println("Inside while loop");
            final int result = dataWriter.get_publication_matched_status(matched);
            if (result != RETCODE_OK.value) {
                System.err.println("ERROR: get_publication_matched_status()" +
                        "failed.");
                return;
            }

            if (matched.value.current_count >= 1) {
                System.out.println("Publisher Matched");
                break;
            }

            ConditionSeqHolder cond = new ConditionSeqHolder(new Condition[]{});
            if (ws.wait(cond, timeout) != RETCODE_OK.value) {
                System.err.println("ERROR: wait() failed.");
                return;

            }

        }

        ws.detach_condition(sc);
    }

    public void write(String txtData) {

        TelemetryDataWriter personDataWriter = TelemetryDataWriterHelper.narrow(dataWriter);
        Telemetry telemetry = new Telemetry();
        telemetry.from = txtData;
        telemetry.telemetryId = 99;
        telemetry.telemetry_data = new String[0];
        String[] data = {"12.34", "13.76"};
        telemetry.telemetry_data = data;


        try {
            int handle = personDataWriter.register_instance(telemetry);
            int ret = RETCODE_TIMEOUT.value;
            System.out.println("Inside write block");


            //write the data
            ret = personDataWriter.write(telemetry, handle);

            if (ret != RETCODE_OK.value) {
                System.err.println("ERROR " +
                        " write() returned " + ret);
            }
            Thread.sleep(100);
        } catch (InterruptedException | NullPointerException ex) {
            ex.printStackTrace();
        }

    }

    //Shutdown
    public void tearDown() {
        System.out.println("Stop the publisher");
        domainParticipant.delete_contained_entities();
        domainParticipantFactory.delete_participant(domainParticipant);
        TheServiceParticipant.shutdown();
        System.out.println("Publisher exiting");
    }

}