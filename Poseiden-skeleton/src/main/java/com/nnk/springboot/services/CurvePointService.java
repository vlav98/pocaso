package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.NotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurvePointService {
    @Autowired
    private CurvePointRepository curvePointRepository;

    public List<CurvePoint> findAll() {
        return curvePointRepository.findAll();
    }

    public CurvePoint findById(Integer id) {
        return curvePointRepository.findById(id).orElseThrow(() -> new NotFoundException("Curve point with id " + id + "not found."));
    }

    public CurvePoint save(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }

    public void update(Integer id, CurvePoint curvePointToUpdate) {
        curvePointToUpdate.setId(id);
        curvePointRepository.save(curvePointToUpdate);
    }

    public void deleteById(Integer id) {
        curvePointRepository.deleteById(id);
    }
}
