package com.resultmanagement.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__({@JsonCreator}))
@ToString
public class Username implements Serializable {

    private String username;
}
