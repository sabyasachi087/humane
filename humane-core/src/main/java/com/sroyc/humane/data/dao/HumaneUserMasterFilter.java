package com.sroyc.humane.data.dao;

public class HumaneUserMasterFilter {

	private String name;
	private Boolean activation;
	private Boolean deletion;

	public static final HumaneUserMasterFilter build(String name, Boolean activation, Boolean deletion) {
		return new HumaneUserMasterFilter(name, activation, deletion);
	}

	public HumaneUserMasterFilter(String name, Boolean activation, Boolean deletion) {
		super();
		this.name = name;
		this.activation = activation;
		this.deletion = deletion;
	}

	public String getName() {
		return name;
	}

	public Boolean getActivation() {
		return activation;
	}

	public Boolean getDeletion() {
		return deletion;
	}

}
