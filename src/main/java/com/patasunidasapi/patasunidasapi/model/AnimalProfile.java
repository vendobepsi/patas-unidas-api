package com.patasunidasapi.patasunidasapi.model;

import java.util.ArrayList;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;



@Entity
@Table(name = "animal_profile")
public class AnimalProfile {
    @Id
    @SequenceGenerator(name="gerador1", sequenceName = "animal_codigo_seq", allocationSize = 1)
    @GeneratedValue(generator = "gerador1", strategy =GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "created_by_user_id")
    private Long createdByUserId;
    @Column(name = "managed_by_user_id")
    private Long managedByUserId;

    private ArrayList<String> photos;
    @Embedded
    private GeoLocation location;
    private AnimalStatus status;
    private Long createdAt;

    private String provisionalName;
    private String description;
    private AnimalSize size;
    private AnimalSex sex;
    private String approximateAge;

    public Long getCreatedByUserId() {
        return createdByUserId;
    }
    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }
    public Long getManagedByUserId() {
        return managedByUserId;
    }
    public void setManagedByUserId(Long managedByUserId) {
        this.managedByUserId = managedByUserId;
    }
    public ArrayList<String> getPhotos() {
        return photos;
    }
    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }
    public GeoLocation getLocation() {
        return location;
    }
    public void setLocation(GeoLocation location) {
        this.location = location;
    }
    public AnimalStatus getStatus() {
        return status;
    }
    public void setStatus(AnimalStatus status) {
        this.status = status;
    }
    public Long getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
    public String getProvisionalName() {
        return provisionalName;
    }
    public void setProvisionalName(String provisionalName) {
        this.provisionalName = provisionalName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public AnimalSize getSize() {
        return size;
    }
    public void setSize(AnimalSize size) {
        this.size = size;
    }
    public AnimalSex getSex() {
        return sex;
    }
    public void setSex(AnimalSex sex) {
        this.sex = sex;
    }
    public String getApproximateAge() {
        return approximateAge;
    }
    public void setApproximateAge(String approximateAge) {
        this.approximateAge = approximateAge;
    }

}
