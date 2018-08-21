package com.sumslack.patterndesign.demo.builder;

public class StepModalBuilder {
	private StepModalBuilder() {
	}
	public static Step1 newBuilder() {
		return new BondSteps();
	}
	public interface Step1 {
		Step2 step1(String name);
	}

	public interface Step2 {
		Step3 step2(String name);
		BuildStep noMoreSteps();
	}
	public interface Step3 {
		BuildStep step3(String name);
	}

	public interface BuildStep {
		StepModal build();
	}
	private static class BondSteps implements Step1,Step2,Step3, BuildStep {
		private String step1,step2,step3;
		@Override
		public BondSteps step3(String name) {
			this.step3 = name;
			return this;
		}
		@Override
		public Step3 step2(String name) {
			this.step2 = name;
			return this;
		}
		@Override
		public Step2 step1(String name) {
			this.step1 = name;
			return this;
		}
		@Override
		public StepModal build() {
			StepModal sm = new StepModal();
			sm.setStep1(step1);
			sm.setStep2(step2);
			sm.setStep3(step3);
			return sm;
		}
		@Override
		public BuildStep noMoreSteps() {
			return this;
		}
	}
}
