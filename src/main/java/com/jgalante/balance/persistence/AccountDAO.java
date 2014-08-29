package com.jgalante.balance.persistence;

import java.math.BigDecimal;

import javax.persistence.TypedQuery;

import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.Transaction;
import com.jgalante.crud.persistence.CrudDAO;

public class AccountDAO extends CrudDAO{

	private static final long serialVersionUID = 1L;

	public BigDecimal currentBalance(Account account) {
//		StringBuffer sb = new StringBuffer();
//		sb.append("SELECT SUM(t.value) FROM ");
//		sb.append(Transaction.class.getName());
//		sb.append(" AS t ");
//		sb.append(" LEFT JOIN t.category c ");
//		sb.append(" LEFT JOIN t.account a ");
//		sb.append(" WHERE c.positive is :positive ");
//		sb.append(" AND a.id = :accountId ");
//		TypedQuery<BigDecimal> query = getEntityManager().createQuery(sb.toString(),BigDecimal.class);
//		query.setParameter("positive", true);
//		query.setParameter("accountId", account.getId());
//		BigDecimal income = query.getSingleResult();
//		if (income == null) income = BigDecimal.ZERO;
//		query.setParameter("positive", false);
//		BigDecimal outcome = query.getSingleResult();
//		if (outcome == null) outcome = BigDecimal.ZERO;
//		return income.subtract(outcome);
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT SUM(CASE c.positive WHEN 1 THEN t.value ELSE (t.value * -1) END) FROM ");
		sb.append(Transaction.class.getName());
		sb.append(" AS t ");
		sb.append(" LEFT JOIN t.category c ");
		sb.append(" LEFT JOIN t.account a ");
		sb.append(" WHERE ");
		sb.append(" a.id = :accountId ");
		TypedQuery<BigDecimal> query = getEntityManager().createQuery(sb.toString(),BigDecimal.class);
		query.setParameter("accountId", account.getId());
		return query.getSingleResult();
	}

}
