package modloader;

public class CustomEvent {
    public String id;
    public EventType eventType;
    public CustomEncounter.Floor floor;
    
    public enum EventType {
        EVENT,
        SHRINE
    }
}