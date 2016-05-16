package dao;

public class Mensagens_Skype {
	
	private long id_geral;
	private long id;
	private String id_sender;
	private String sender_display_name;
	private String content;
	private String chat;
	private String message_type;
	private Mensagens_Skype_Dao objPersistente;		
	
	public String getMessage_type() {	return message_type; }
	public void setMessage_type(String message_type) { this.message_type = message_type; }
	public Mensagens_Skype_Dao getObjPersistente() { return objPersistente; }
	public void setObjPersistente(Mensagens_Skype_Dao objPersistente) { this.objPersistente = objPersistente; }
	public long getId_geral() { return id_geral; }
	public void setId_geral(long id_geral) { this.id_geral = id_geral; }
	public long getId() { return id; }
	public void setId(long id) { this.id = id; }
	public String getId_sender() { return id_sender; }
	public void setId_sender(String id_sender) { this.id_sender = id_sender; }
	public String getSender_display_name() { return sender_display_name; } 
	public void setSender_display_name(String sender_display_name) { this.sender_display_name = sender_display_name; }
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
	public String getChat() { return chat; }
	public void setChat(String chat) { this.chat = chat; }
	
    public Mensagens_Skype() {

    	objPersistente = new Mensagens_Skype_Dao(this);
    	
    }
    
    public void criaObjPersistente() {
    	
    	objPersistente = new Mensagens_Skype_Dao(this);
    	
    }
    
    public void finalize() {
    	
    	if (objPersistente != null)
    		objPersistente = null;
    	
    }
}