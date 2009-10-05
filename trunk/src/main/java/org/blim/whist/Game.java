package org.blim.whist;

import com.google.common.collect.Lists;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Game {

	private Long id;
	
	@Id
	public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
	
}
