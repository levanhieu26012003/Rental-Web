/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lvh.RentalBE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author levan
 */
@Entity
@Table(name = "motel")
@NamedQueries({
        @NamedQuery(name = "Motel.findAll", query = "SELECT m FROM Motel m"),
        @NamedQuery(name = "Motel.findById", query = "SELECT m FROM Motel m WHERE m.id = :id"),
        @NamedQuery(name = "Motel.findByActive", query = "SELECT m FROM Motel m WHERE m.active = :active"),
        @NamedQuery(name = "Motel.findByCreatedDate", query = "SELECT m FROM Motel m WHERE m.createdDate = :createdDate"),
        @NamedQuery(name = "Motel.findByUpdatedDate", query = "SELECT m FROM Motel m WHERE m.updatedDate = :updatedDate"),
        @NamedQuery(name = "Motel.findByTitle", query = "SELECT m FROM Motel m WHERE m.title = :title"),
        @NamedQuery(name = "Motel.findByAddress", query = "SELECT m FROM Motel m WHERE m.address = :address"),
        @NamedQuery(name = "Motel.findByPrice", query = "SELECT m FROM Motel m WHERE m.price = :price"),
        @NamedQuery(name = "Motel.findByArea", query = "SELECT m FROM Motel m WHERE m.area = :area"),
        @NamedQuery(name = "Motel.findByNumberTenant", query = "SELECT m FROM Motel m WHERE m.numberTenant = :numberTenant"),
        @NamedQuery(name = "Motel.findByWards", query = "SELECT m FROM Motel m WHERE m.wards = :wards"),
        @NamedQuery(name = "Motel.findByDistrict", query = "SELECT m FROM Motel m WHERE m.district = :district"),
        @NamedQuery(name = "Motel.findByProvince", query = "SELECT m FROM Motel m WHERE m.province = :province"),
        @NamedQuery(name = "Motel.findByStatus", query = "SELECT m FROM Motel m WHERE m.status = :status")})
public class Motel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "active")
    private Boolean active = true;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @Column(name = "address")
    private String address;
    @Column(name = "price")
    private Integer price;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "area")
    private Float area;
    @Column(name = "number_tenant")
    private Integer numberTenant;
    @Basic(optional = false)
    @Column(name = "wards")
    private String wards;
    @Basic(optional = false)
    @Column(name = "district")
    private String district;
    @Basic(optional = false)
    @Column(name = "province")
    private String province;
    @Column(name = "status")
    private String status;
    @Column(name = "lat")
    private Double lat;
    @Column(name = "lng")
    private Double lng;
    @JsonIgnore
    @OneToMany(mappedBy = "motelId", cascade = CascadeType.REMOVE)
    private Collection<Image> imageCollection;
    @JsonIgnore
    @OneToMany(mappedBy = "houseId",cascade = CascadeType.REMOVE)
    private Collection<Comment> commentCollection;
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne( fetch = FetchType.LAZY)
    private User userId;

    @Transient
    private List<MultipartFile> files;

    public Motel() {
    }

    public Motel(Long id) {
        this.id = id;
    }

    public Motel(Long id, String title, String address, String wards, String district, String province) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.wards = wards;
        this.district = district;
        this.province = province;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Float getArea() {
        return area;
    }

    public void setArea(Float area) {
        this.area = area;
    }

    public Integer getNumberTenant() {
        return numberTenant;
    }

    public void setNumberTenant(Integer numberTenant) {
        this.numberTenant = numberTenant;
    }

    public String getWards() {
        return wards;
    }

    public void setWards(String wards) {
        this.wards = wards;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Collection<Image> getImageCollection() {
        return imageCollection;
    }

    public void setImageCollection(Collection<Image> imageCollection) {
        this.imageCollection = imageCollection;
    }

    public Collection<Comment> getCommentCollection() {
        return commentCollection;
    }

    public void setCommentCollection(Collection<Comment> commentCollection) {
        this.commentCollection = commentCollection;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Motel)) {
            return false;
        }
        Motel other = (Motel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.lvh.pojo.Motel[ id=" + id + " ]";
    }

    @PrePersist
    protected void onCreate() {
        createdDate = new Date();
        updatedDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = new Date();
    }
}
