package com.mixfa;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MethodInterceptionDescription) obj;
        return Objects.equals(this.matcher, that.matcher) &&
                Objects.equals(this.implementation, that.implementation) &&
                this.beforeSuper == that.beforeSuper;
    }

    @Override
    public int hashCode() {
        return Objects.hash(matcher, implementation, beforeSuper);
    }

    @Override
    public String toString() {
        return "MethodInterceptionDescription[" +
                "matcher=" + matcher + ", " +
                "implementation=" + implementation + ", " +
                "beforeSuper=" + beforeSuper + ']';
    }


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