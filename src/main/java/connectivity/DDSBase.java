package connectivity;

import DDS.DomainParticipant;
import DDS.DomainParticipantFactory;
import DDS.PARTICIPANT_QOS_DEFAULT;
import OpenDDS.DCPS.DEFAULT_STATUS_MASK;
import OpenDDS.DCPS.TheParticipantFactory;
import org.omg.CORBA.StringSeqHolder;

public final class DDSBase {
    private DomainParticipantFactory domainParticipantFactory;
    private DomainParticipant domainParticipant;
    private String[] args;
    private int domainID;
    private static DDSBase ddsBase;


    public static DDSBase getInstance(){
        if (ddsBase == null){
            ddsBase = new DDSBase();
        }
        return ddsBase;
    }

    public  void setup(String[] args, int domainID){
        this.args = args;
        this.domainID = domainID;
        domainParticipantFactory = TheParticipantFactory.WithArgs(new StringSeqHolder(args));
        if (domainParticipantFactory == null) {
            System.err.println("ERROR: Domain Participant Factory not found");
            return;
        }

        //Create the domain Participant
        domainParticipant = domainParticipantFactory.create_participant(domainID,
                PARTICIPANT_QOS_DEFAULT.get(), null, DEFAULT_STATUS_MASK.value);
        if (domainParticipant == null) {
            System.err.println("ERROR: Domain Participant creation failed");
            return;
        }

    }

    public DomainParticipantFactory getDomainParticipantFactory(){
        return domainParticipantFactory;
    }

    public DomainParticipant getDomainParticipant(){
        return domainParticipant;
    }

    public String[] getArgs(){
        return args;
    }

    public int getDomainID(){
        return domainID;
    }
}
