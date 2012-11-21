update studentmap
set section_c = subq.sec
from (
	select rea2.id as sid, rea2.section_c as sec, rea2.count
	from
		(select id, max(count)
		from
			(select sm.id, tm.section_c, count(tm.section_c)
			from 
				studentmap sm,
				subscribed s,
				coursemap cm,
				topicmap tm
				
			where 
				(sm.section_c = 20 OR sm.section_c = 30)
				AND s.student = sm.id
				AND s.course = cm.id
				AND cm.topic = tm.id
				AND tm.section_c != 20 AND tm.section_c != 30
			group by 
				sm.id, tm.section_c) AS rea
		group by id) as outer_rea,
		(select sm.id, tm.section_c, count(tm.section_c)
			from 
				studentmap sm,
				subscribed s,
				coursemap cm,
				topicmap tm
				
			where 
				(sm.section_c = 20 OR sm.section_c = 30)
				AND s.student = sm.id
				AND s.course = cm.id
				AND cm.topic = tm.id
				AND tm.section_c != 20 AND tm.section_c != 30
			group by 
				sm.id, tm.section_c) AS rea2
	where outer_rea.id = rea2.id
		and outer_rea.max = rea2.count
		) as subq
where studentmap.id = subq.sid
