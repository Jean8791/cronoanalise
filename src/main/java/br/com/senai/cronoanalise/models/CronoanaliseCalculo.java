package br.com.senai.cronoanalise.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "cronoanalise_calculo")
public class CronoanaliseCalculo extends AbstractEntity implements Serializable {
}
