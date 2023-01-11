package org.alive.learn.event;

import java.util.EventObject;

/**
 * Event
 * 
 * @author hailin84
 *
 */
public class CusEvent extends EventObject {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7095992975323956423L;

	private Object source;//ÊÂ¼þÔ´  
    
    public CusEvent(Object source){  
        super(source);  
        this.source = source;  
    }  
  
    public Object getSource() {  
        return source;  
    }  
  
    public void setSource(Object source) {  
        this.source = source;  
    }  
}
