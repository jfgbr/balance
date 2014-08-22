package com.jgalante.crud.facade;

import java.util.List;
import java.util.Map;

import com.jgalante.balance.facade.IController;
import com.jgalante.balance.facade.IDAO;
import com.jgalante.crud.entity.BaseEntity;

public interface ICrudController<T extends BaseEntity, D extends IDAO>
		extends IController<T, D> {

	public List<T> search(int first, int pageSize, Map<String, Boolean> sort,
			Map<String, Object> filters);

	public int rowCount();
}
