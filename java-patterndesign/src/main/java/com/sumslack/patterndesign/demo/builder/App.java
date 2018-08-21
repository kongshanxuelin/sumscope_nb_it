package com.sumslack.patterndesign.demo.builder;

public class App {
	public static void main(String[] args) {
		Bond bond = new Bond.Builder("111")
				.withShortName("abc")
				.withTerm("ttt")
				.withIssuer(new Issuer())
				.withRate(new Rate())
				.build();
		System.out.println(bond);
		
		//如果有顺序的Builder
		StepModal sm = StepModalBuilder.newBuilder()
				.step1("xxxx")
				.step2("yyy")
				.step3("zzz")
				.build();
		sm = StepModalBuilder.newBuilder()
				.step1("xxxmust")
				.noMoreSteps()
				.build();
	}
}
