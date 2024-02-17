package com.mixfa.bytebuddy_proxy;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;

public final class MethodInterceptionDescription {
    private final ElementMatcher<MethodDescription> matcher;
    private final Implementation.Composable implementation;
    private final boolean beforeSuper;

    public MethodInterceptionDescription(ElementMatcher<MethodDescription> matcher,
                                         Implementation.Composable implementation,
                                         boolean beforeSuper) {
        this.matcher = matcher;
        this.implementation = implementation;
        this.beforeSuper = beforeSuper;
    }

    public ElementMatcher<MethodDescription> matcher() {
        return matcher;
    }

    public Implementation.Composable implementation() {
        return implementation;
    }

    public boolean beforeSuper() {
        return beforeSuper;
    }

    public static class Builder {
        private ElementMatcher<MethodDescription> matcher;
        private Implementation.Composable implementation;
        private boolean beforeSuper = false;

        @SuppressWarnings("unchecked")
        public final Builder setMatchers(ElementMatcher<MethodDescription>... matchers) {
            this.matcher = new ElementMatcher.Junction.Conjunction<>(matchers);
            return this;
        }

        public Builder setMatcher(ElementMatcher<MethodDescription> matcher) {
            this.matcher = matcher;
            return this;
        }

        public Builder setImlp(Implementation.Composable implementation) {
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