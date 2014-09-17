package com.jgalante.balance.persistence;

import java.util.Calendar;
import java.util.List;

import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.Balance;
import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Compare;
import com.jgalante.balance.entity.Transaction;

public class CompareDAO extends BaseDAO {

	private static final long serialVersionUID = 1L;

	public List<Compare> findCompares(Account account, Category category,
			Calendar calendar) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT  NEW ");
		sb.append(Compare.class.getName());
		sb.append(" (b.account, (CASE b.positive WHEN 1 THEN b.value ELSE (b.value * -1) END), (CASE t.positive WHEN 1 THEN t.value ELSE (t.value * -1) END)) FROM ");
		sb.append(Balance.class.getName());
		sb.append(" AS b ");
		sb.append(" INNER JOIN ");
		sb.append(Transaction.class.getName());
		sb.append(" AS t ");
		sb.append(" WHERE ");
		
		return null;
	}
	
	
	
}
