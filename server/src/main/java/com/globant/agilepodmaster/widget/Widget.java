package com.globant.agilepodmaster.widget;

import javax.persistence.Entity;

import lombok.Data;

import com.globant.agilepodmaster.core.AbstractEntity;

@Entity
@Data
public class Widget extends AbstractEntity {
    private String name;
    private String title;
    private String viewName;
    private int width;
    private int height;
}