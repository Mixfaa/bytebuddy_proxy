package com.mixfa;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.matcher.ElementMatcher;

public record MethodInterceptionDescription(ElementMatcher<MethodDescription> matcher,
                                     Implementation.Composable implementation,
                                     boolean beforeSuper) {

    public static class Builder {
        private ElementMatcher<MethodDescription> matcher;
        private Implementation.Composable implementation;
        private boolean beforeSuper = false;

        @SuppressWarnings("unchecked")
        public final Builder matchers(ElementMatcher<MethodDescription>... matchers) {
            this.matcher = new ElementMatcher.Junction.Conjunction<>(matchers);
            return this;
        }

        public Builder matcher(ElementMatcher<MethodDescription> matcher) {
            this.matcher = matcher;
            return this;
        }

        public Builder implementation(Implementation.Composable implementation) {
            this.implementation = implementation;
            return this;
        }

        public Builder beforeSuper() {
            this.beforeSuper = true;
            return this;
        }

        public Builder afterSuper() {
            this.beforeSuper = false;
            return this;
        }

        public MethodInterceptionDescription build() {
            return new MethodInterceptionDescription(matcher, implementation, beforeSuper);
        }
    }
}