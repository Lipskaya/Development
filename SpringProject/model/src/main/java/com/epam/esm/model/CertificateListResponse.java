package com.epam.esm.model;

import java.util.List;

/**
 * Response model for paginated list of certificates
 */

public class CertificateListResponse {

  private int page;
  private int itemsPerPage;
  private int totalItems;
  private List<Certificate> certificates;

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getItemsPerPage() {
    return itemsPerPage;
  }

  public void setItemsPerPage(int itemsPerPage) {
    this.itemsPerPage = itemsPerPage;
  }

  public int getTotalItems() {
    return totalItems;
  }

  public void setTotalItems(int totalItems) {
    this.totalItems = totalItems;
  }

  public List<Certificate> getCertificates() {
    return certificates;
  }

  public void setCertificates(List<Certificate> certificates) {
    this.certificates = certificates;
  }

  @Override
  public String toString() {
    return "CertificateListResponse{" +
        "page=" + page +
        ", itemsPerPage=" + itemsPerPage +
        ", totalItems=" + totalItems +
        ", certificates=" + certificates +
        '}';
  }
}
