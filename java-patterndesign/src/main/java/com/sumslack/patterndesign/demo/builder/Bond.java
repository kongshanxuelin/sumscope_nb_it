package com.sumslack.patterndesign.demo.builder;

public class Bond {
	private String id;
	private String shortName;
	private String term;

	private Issuer issuer;
	private Rate rate;

	private Bond(Builder builder) {
		this.id = builder.id;
		this.shortName = builder.shortName;
		this.term = builder.term;
		this.issuer = builder.issuer;
		this.rate = builder.rate;
	}

	public String getId() {
		return id;
	}

	public String getShortName() {
		return shortName;
	}

	public String getTerm() {
		return term;
	}

	public Issuer getIssuer() {
		return issuer;
	}

	public Rate getRate() {
		return rate;
	}
	
	

	@Override
	public String toString() {
		return "Bond [id=" + id + ", shortName=" + shortName + ", term=" + term + ", issuer=" + issuer + ", rate="
				+ rate + "]";
	}



	public static class Builder {
		private String id;
		private String shortName;
		private String term;

		private Issuer issuer;
		private Rate rate;

		public Builder(String id) {
			this.id = id;
		}

		public Builder withShortName(String shortName) {
			this.shortName = shortName;
			return this;
		}

		public Builder withTerm(String term) {
			this.term = term;
			return this;
		}

		public Builder withIssuer(Issuer issuer) {
			this.issuer = issuer;
			return this;
		}

		public Builder withRate(Rate rate) {
			this.rate = rate;
			return this;
		}

		public Bond build() {
			return new Bond(this);
		}
	}
}
