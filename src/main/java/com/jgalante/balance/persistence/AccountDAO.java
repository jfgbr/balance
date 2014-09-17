package com.jgalante.balance.persistence;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.Transaction;
import com.jgalante.crud.entity.BaseEntity;
import com.jgalante.crud.persistence.CrudDAO;
import com.jgalante.crud.util.Util;

public class AccountDAO extends CrudDAO{

	private static final long serialVersionUID = 1L;
	
	@Override
	public <T extends BaseEntity> List<T> searchAll(Map<String, Boolean> sort) {
		getQueryParam().reset();
		addJoinFields("type");
		Map<String, Boolean> tmpSort = new LinkedHashMap<String, Boolean>();
		if (sort != null) {
			tmpSort.putAll(sort);
		}
		tmpSort.put("id", false);
		return super.searchAll(tmpSort);
	}
	
	public BigDecimal currentBalance(Account account) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT SUM(CASE c.positive WHEN 1 THEN t.value ELSE (t.value * -1) END) FROM ");
		sb.append(Transaction.class.getName());
		sb.append(" AS t ");
		sb.append(" LEFT JOIN t.category c ");
		sb.append(" LEFT JOIN t.account a ");
		if (account != null) {
			sb.append(" WHERE ");
			sb.append(" a.id = ");
			sb.append(account.getId());			
		}
		TypedQuery<BigDecimal> query = getEntityManager().createQuery(sb.toString(),BigDecimal.class);
		return query.getSingleResult();
	}
	
//	@Transactional
	public Account findBalanceTransaction(Account account, Calendar startDate, Calendar endDate) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT a FROM ");
		sb.append(Account.class.getName());
		sb.append(" AS a ");
//		sb.append(" JOIN FETCH a.transactions t ");
		sb.append(" JOIN FETCH a.balances b ");
		sb.append(" WHERE a.id = :accountId");
		sb.append(" AND (b.balanceDate >= :dtStart AND b.balanceDate <= :dtEnd) ");
//		sb.append(" AND (t.transactionDate >= :dtStart AND t.transactionDate <= :dtEnd) ");
		sb.append(" ORDER BY  b.balanceDate ASC ");
		
		TypedQuery<Account> query = getEntityManager().createQuery(sb.toString(),Account.class);
		query.setParameter("accountId", account.getId());
		query.setParameter("dtStart", Util.beginOfMonth(startDate), TemporalType.DATE);
		query.setParameter("dtEnd", Util.endOfMonth(endDate), TemporalType.DATE);
		
		return query.getSingleResult();
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return Account.class;
	}

}
