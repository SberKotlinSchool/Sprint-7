prepare p1(int) as select * from account1 a where id = $1;

explain (analyze, buffers, verbose) execute p1(1);

/*
QUERY PLAN                                                                                                                   |
-----------------------------------------------------------------------------------------------------------------------------+
Index Scan using account_pk on public.account1 a  (cost=0.15..8.17 rows=1 width=16) (actual time=0.010..0.010 rows=1 loops=1)|
  Output: id, amount, version                                                                                                |
  Index Cond: (a.id = 1)                                                                                                     |
  Buffers: shared hit=2                                                                                                      |
Planning:                                                                                                                    |
  Buffers: shared hit=19                                                                                                     |
Planning Time: 1.780 ms                                                                                                      |
Execution Time: 0.018 ms                                                                                                     |
*/

deallocate p1;

-- Как видно из плана, для доступа к данным используется Index Scan по account_pk (это PK).
-- Эффективнее данного вида доступа только доступ по CTID - физическому адресу блока.
-- Аналогично в других запросах - везде в предикатах используется PK.