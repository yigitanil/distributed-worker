package org.ygt.data.model.table;

import lombok.Data;
import org.ygt.data.model.enums.Status;

import javax.persistence.*;


@Data
@Entity
public class UrlStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String url;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String httpCode;


}
