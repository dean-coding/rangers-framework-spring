package com.pxtec.bxj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * city实体 
 * @version 1.0
 * @author fuhw
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class City extends BaseEntity {
    private String name;

    private String state;

	public City( String name, String state ) {
		super();
		this.name = name;
		this.state = state;
	}

	public City() {
		super();
	}
    
    
}
