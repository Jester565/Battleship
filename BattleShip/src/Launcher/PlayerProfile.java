package Launcher;

import java.io.Serializable;

public class PlayerProfile implements Serializable{

	private static final long serialVersionUID = 7072462510362522678L;
	private String name;
	private Double exp = 0d;
	public PlayerProfile(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public int getExp(){
		return exp.intValue();
	}
	public int getRank(){
		return (int)(exp/2000d);
	}
	public void addExp(double exp){
		this.exp += exp;
	}
}
