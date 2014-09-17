package com.jgalante.balance.persistence;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.Balance;
import com.jgalante.crud.entity.BaseEntity;
import com.jgalante.crud.persistence.CrudDAO;
import com.jgalante.crud.util.Util;

public class BalanceDAO extends CrudDAO {

	private static final long serialVersionUID = 1L;
	
	@Override
	public <T extends BaseEntity> List<T> searchAll(Map<String, Boolean> sort) {
		getQueryParam().reset();
		addJoinFields("account");
		Map<String, Boolean> tmpSort = new LinkedHashMap<String, Boolean>();
		if (sort != null) {
			tmpSort.putAll(sort);
		}
		tmpSort.put("id", false);
		return super.searchAll(tmpSort);
	}
	
	public BigDecimal currentBalance(Account account) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT SUM(CASE b.positive WHEN 1 THEN b.value ELSE (b.value * -1) END) FROM ");
		sb.append(Balance.class.getName());
		sb.append(" AS b ");
		sb.append(" LEFT JOIN b.account a ");
		sb.append(" WHERE a.type <> 3 ");
		sb.append(" AND (b.balanceDate >= :dtStart AND b.balanceDate <= :dtEnd) ");
		if (account != null) {
			int pos = sb.indexOf("SUM");
			sb.delete(pos, pos+3);
			sb.append(" AND ");
			sb.append(" a.id = ");
			sb.append(account.getId());			
		}
		TypedQuery<BigDecimal> query = getEntityManager().createQuery(sb.toString(),BigDecimal.class);
		
		query.setParameter("dtStart", Util.beginOfMonth(Calendar.getInstance()), TemporalType.DATE);
		query.setParameter("dtEnd", Util.endOfMonth(Calendar.getInstance()), TemporalType.DATE);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		if (super.getEntityClass() == null) {
			super.setEntityClass(Balance.class);
		}
		return super.getEntityClass();
	}

}
