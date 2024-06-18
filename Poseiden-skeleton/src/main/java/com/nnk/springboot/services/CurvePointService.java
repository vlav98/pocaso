package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurvePointService {
    @Autowired
    private CurvePointRepository curvePointRepository;

    public List<CurvePoint> findAll() {
        return curvePointRepository.findAll();
    }

    public CurvePoint findById(Integer id) {
        Optional<CurvePoint> optionalCurvePoint = curvePointRepository.findById(id);
        return optionalCurvePoint.orElse(null);
    }

    public CurvePoint save(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }

    public void update(Integer id, CurvePoint curvePoint) {
        curvePoint.setId(id);
        curvePointRepository.save(curvePoint);
    }

    public void deleteById(Integer id) {
        curvePointRepository.deleteById(id);
    }
}
