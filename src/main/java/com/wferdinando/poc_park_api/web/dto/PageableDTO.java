package com.wferdinando.poc_park_api.web.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageableDTO {

    private List content = new ArrayList();
    private boolean first;
    private boolean last;
    @JsonProperty("page")
    private int number;
    private int size;
    private int totalElements;
    private int totalPages;
    @JsonProperty("pageElements")
    private int numberOfElements;

}
