package com.epam.esm.service.mapper;

import com.epam.esm.entity.CertificateEntity;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CertificateEntityMapper {

  @Autowired
  private TagEntityMapper tagEntityMapper;

  public CertificateEntity mapToEntity(Certificate certificate) {
    CertificateEntity certificateEntity = new CertificateEntity();
    certificateEntity.setId(certificate.getId());
    certificateEntity.setCreateDay(certificate.getCreateDay());
    certificateEntity.setLastUpdateDate(certificate.getLastUpdateDate());
    certificateEntity.setDescription(certificate.getDescription());
    certificateEntity.setDuration(certificate.getDuration());
    certificateEntity.setName(certificate.getName());
    certificateEntity.setPrice(certificate.getPrice());

    Set<TagEntity> tagEntities = new HashSet<>();

    for (Tag t: certificate.getTags() ) {
      tagEntities.add(tagEntityMapper.mapToEntity(t));
    }

    certificateEntity.setTagEntities(tagEntities);
    return certificateEntity;
  }

  public Certificate mapToModel (CertificateEntity certificateEntity){
    Certificate certificate = new Certificate();
    certificate.setId(certificateEntity.getId());
    certificate.setCreateDay(certificateEntity.getCreateDay());
    certificate.setLastUpdateDate(certificateEntity.getLastUpdateDate());
    certificate.setDescription(certificateEntity.getDescription());
    certificate.setDuration(certificateEntity.getDuration());
    certificate.setName(certificateEntity.getName());
    certificate.setPrice(certificateEntity.getPrice());

    Set<Tag> tags = new HashSet<>();

    for (TagEntity t: certificateEntity.getTagEntities() ) {
      tags.add(tagEntityMapper.mapToModel(t));
    }

    certificate.setTags(tags);
    return certificate;
  }

}
