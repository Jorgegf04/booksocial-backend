package com.example.booksocial_backend.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import com.example.booksocial_backend.DTO.catalog.VolumeRequestDTO;
import com.example.booksocial_backend.domain.catalog.Edition;
import com.example.booksocial_backend.domain.catalog.Volume;
import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.repository.EditionRepository;
import com.example.booksocial_backend.repository.VolumeRepository;
import com.example.booksocial_backend.service.impl.VolumeServiceImpl;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
/**
 * Tests unitarios de {@link com.example.booksocial_backend.service.impl.VolumeServiceImpl}.
 *
 * <p>Cubre la creación de volúmenes de cómic dentro de una edición con validación de número
 * de volumen positivo y único por edición, la búsqueda individual y en lista (todos /
 * por edición / ordenados por número), la actualización y la eliminación.</p>
 *
 * @author Jorge
 * @version 1.4
 * @since 2026-04-22
 */
class VolumeServiceImplTest {

  @Mock
  private VolumeRepository volumeRepository;

  @Mock
  private EditionRepository editionRepository;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private VolumeServiceImpl service;

  private Volume volume;
  private Edition edition;

  @BeforeEach
  void setUp() {

    Work work = new Work();
    work.setId(1L);
    work.setTitle("Naruto");

    edition = new Edition();
    edition.setId(1L);
    edition.setTitle("Naruto Vol 1");
    edition.setIsbn("978-1234567890");
    edition.setWork(work);

    volume = new Volume();
    volume.setId(1L);
    volume.setVolumeNumber(1);
    volume.setTitle("Volumen 1");
    volume.setEdition(edition);
  }

  // =========================
  // CREATE
  // =========================

  @Test
  void shouldCreateVolumeSuccessfully() {

    VolumeRequestDTO request = new VolumeRequestDTO(1, "Volumen 1", 1L);

    when(modelMapper.map(request, Volume.class)).thenReturn(volume);
    when(editionRepository.findById(1L)).thenReturn(Optional.of(edition));
    when(volumeRepository.findByEditionId(1L)).thenReturn(List.of());
    when(volumeRepository.save(any())).thenReturn(volume);

    var result = service.createVolume(request);

    assertNotNull(result);
    assertEquals("Volumen 1", result.getTitle());
    assertEquals(1, result.getVolumeNumber());
  }

  @Test
  void shouldThrowExceptionWhenEditionNotFound() {

    VolumeRequestDTO request = new VolumeRequestDTO(1, "Volumen 1", 1L);

    when(modelMapper.map(request, Volume.class)).thenReturn(volume);
    when(editionRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> service.createVolume(request));
  }

  @Test
  void shouldThrowExceptionWhenDuplicateVolumeNumber() {

    VolumeRequestDTO request = new VolumeRequestDTO(1, "Volumen 1", 1L);

    when(modelMapper.map(request, Volume.class)).thenReturn(volume);
    when(editionRepository.findById(1L)).thenReturn(Optional.of(edition));
    when(volumeRepository.findByEditionId(1L)).thenReturn(List.of(volume));

    assertThrows(IllegalArgumentException.class, () -> service.createVolume(request));
  }

  @Test
  void shouldThrowExceptionWhenVolumeNumberInvalid() {

    volume.setVolumeNumber(0);
    VolumeRequestDTO request = new VolumeRequestDTO(0, "Volumen 1", 1L);

    when(modelMapper.map(request, Volume.class)).thenReturn(volume);
    when(editionRepository.findById(1L)).thenReturn(Optional.of(edition));
    when(volumeRepository.findByEditionId(1L)).thenReturn(List.of());

    assertThrows(IllegalArgumentException.class, () -> service.createVolume(request));
  }

  // =========================
  // READ
  // =========================

  @Test
  void shouldGetVolumeById() {

    when(volumeRepository.findById(1L)).thenReturn(Optional.of(volume));

    var result = service.getVolumeById(1L);

    assertEquals(1L, result.getId());
    assertEquals("Volumen 1", result.getTitle());
  }

  @Test
  void shouldThrowExceptionWhenVolumeNotFound() {

    when(volumeRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> service.getVolumeById(1L));
  }

  @Test
  void shouldGetAllVolumes() {

    when(volumeRepository.findAll()).thenReturn(List.of(volume));

    var result = service.getAllVolumes();

    assertEquals(1, result.size());
  }

  @Test
  void shouldGetVolumesByEdition() {

    when(volumeRepository.findByEditionId(1L)).thenReturn(List.of(volume));

    var result = service.getVolumesByEdition(1L);

    assertEquals(1, result.size());
  }

  @Test
  void shouldGetVolumesByEditionOrdered() {

    when(volumeRepository.findByEditionIdOrderByVolumeNumberAsc(1L)).thenReturn(List.of(volume));

    var result = service.getVolumesByEditionOrdered(1L);

    assertEquals(1, result.size());
  }

  // =========================
  // UPDATE
  // =========================

  @Test
  void shouldUpdateVolumeSuccessfully() {

    VolumeRequestDTO request = new VolumeRequestDTO(2, "Volumen 2", 1L);

    Volume updated = new Volume();
    updated.setVolumeNumber(2);
    updated.setTitle("Volumen 2");
    updated.setEdition(edition);

    when(volumeRepository.findById(1L)).thenReturn(Optional.of(volume));
    when(modelMapper.map(request, Volume.class)).thenReturn(updated);
    when(editionRepository.findById(1L)).thenReturn(Optional.of(edition));
    when(volumeRepository.findByEditionId(1L)).thenReturn(List.of(volume));
    when(volumeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = service.updateVolume(1L, request);

    assertEquals(2, result.getVolumeNumber());
    assertEquals("Volumen 2", result.getTitle());
  }

  // =========================
  // DELETE
  // =========================

  @Test
  void shouldDeleteVolume() {

    when(volumeRepository.findById(1L)).thenReturn(Optional.of(volume));

    service.deleteVolume(1L);

    verify(volumeRepository).delete(volume);
  }

  @Test
  void shouldThrowExceptionWhenDeletingNonExistingVolume() {

    when(volumeRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> service.deleteVolume(1L));
  }
}
