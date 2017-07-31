package br.eti.archanjo.webcron.pojo;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;

@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class EnvironmentDTO implements Serializable {
    private static final long serialVersionUID = 339174635727478627L;
    private String key;
    private String value;
    private Date created;
    private Date modified;

    @Override
    public String toString() {
        return "EnvironmentDTO{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EnvironmentDTO that = (EnvironmentDTO) o;

        return new EqualsBuilder()
                .append(key, that.key)
                .append(value, that.value)
                .append(created, that.created)
                .append(modified, that.modified)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(key)
                .append(value)
                .append(created)
                .append(modified)
                .toHashCode();
    }
}
