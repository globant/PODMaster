package com.globant.agilepodmaster.dashboard;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DashboardRepository extends PagingAndSortingRepository<Dashboard, Long> {
}
