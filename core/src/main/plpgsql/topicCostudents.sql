-- get the costudents quantity between two topics
-- usage : select topicCostudents('{SC,IN}'::varchar[],NULL,NULL)
CREATE OR REPLACE FUNCTION topicCostudents(
	IN sname character varying[],
	IN from_sem int,
	IN to_sem int,
	OUT t_id1 int,
	OUT t_id2 int,
	OUT t_sum int
	)
RETURNS SETOF record AS
$$
DECLARE
  retval RECORD;
BEGIN
  CREATE TEMP TABLE topic_costudents
	ON COMMIT DROP
	AS
	SELECT 
		sum(crm.value) AS sum,
		tm1.name AS name1,
		tm1.id AS id1,
		tm2.name AS name2,
		tm2.id AS id2
	FROM 
		courserelationmap crm,
		coursemap cm1,
		coursemap cm2,
		topicmap tm1,
		topicmap tm2,
		sectionmap sm1,
		sectionmap sm2
	WHERE 
		crm.relation::text = 'costudents'::text 
		AND crm.from_c = cm1.id 
		AND cm1.topic = tm1.id 
		AND crm.to_c = cm2.id 
		AND cm2.topic = tm2.id
		AND tm1.section_c = sm1.id
		AND tm2.section_c = sm2.id
		AND sm1.name ilike any(sname)
		AND sm2.name ilike any(sname)
	GROUP BY 
		tm1.name,
		tm1.id,
		tm2.name,
		tm2.id
;
  FOR retval IN	
	SELECT 
		tc1.name1,
		tc1.id1,
		tc1.name2,
		tc1.id2,
		(tc1.sum + tc2.sum) as sum
	FROM 
		topic_costudents tc1,
		topic_costudents tc2	

	WHERE 
		tc1.id1 = tc2.id2
		AND tc2.id1 = tc1.id2
		AND tc1.id2 < tc1.id1
  LOOP
    t_id1  := retval.id1;
    t_id2 := retval.id2;
    t_sum := retval.sum;
    RETURN NEXT;
  END LOOP;
  RETURN;
END;
$$
LANGUAGE plpgsql;

