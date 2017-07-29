package br.eti.archanjo.webcron.pojo;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.nio.file.Path;

@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class JobResult implements Serializable {
    private static final long serialVersionUID = 1948094618729805585L;
    private Path tmpFile;
    private int exitValue;

    @Override
    public String toString() {
        return "JobResult{" +
                "tmpFile=" + tmpFile +
                ", exitValue=" + exitValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        JobResult jobResult = (JobResult) o;

        return new EqualsBuilder()
                .append(exitValue, jobResult.exitValue)
                .append(tmpFile, jobResult.tmpFile)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(tmpFile)
                .append(exitValue)
                .toHashCode();
    }
}
