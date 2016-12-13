package com.infinera.dao;

import java.util.List;

public class QueryResult<E> {
	private int count;
	private List<E> list;

	public QueryResult(int count, List<E> list) {
		super();
		this.count = count;
		this.list = list;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

}
