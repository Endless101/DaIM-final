package databases;

public class QueriesMDB {

    public String E1 =

            "SELECT publisher " +
            "FROM proceedings p INNER JOIN conference c ON p.proceedings_key = c.proceedings_key " +
            "WHERE c.title = \"' PODS '\" LIMIT 1;";

    public String E2 =

            "select ar.title " +
            "from article ar " +
            "join author a on ar.article_key = a.publication_key and  a.author = \"'Martin Grohe'\" " +
            "join journal j on ar.article_key = j.article_key and j.title = \"' Theor. Comput. Sci. '\" " +
            "order by ar.title;";

    public String M1 =

            "select count(distinct inproceeding_key) " +
            "from inproceeding " +
            "inner join conference " +
            "on inproceeding_key = conference.proceedings_key " +
            "where inproceeding.year = \"'2022'\" AND conference.title = \"' SIGMOD Conference '\";";

    public String M2 =

            "SELECT COUNT(*) AS Amount, j.title " +
            "FROM article a INNER JOIN journal j ON a.article_key = j.article_key " +
            "WHERE Year = ( SELECT Min(Year) FROM article) " +
            "GROUP BY Journal;";

    public String M3 =

            "SELECT DISTINCT PERCENTILE_CONT(0.5) " +
            "WITHIN GROUP (ORDER BY NumberOfArticles) OVER () AS median " +
            "FROM (SELECT COUNT(inproceeding_key) AS NumberOfArticles " +
            "FROM inproceeding i " +
            "INNER JOIN conference c ON i.inproceeding_key = c.proceedings_key " +
            "WHERE c.title = \"' CIDR '\" " +
            "GROUP BY i.year) t;";

    public String M4 =

            "select i.year " +
            "from author a " +
            "inner join conference c on c.proceedings_key = a.publication_key " +
            "inner join inproceeding i on i.inproceeding_key = a.publication_key " +
            "where c.title = \"' SIGMOD Conference '\" " +
            "group by c.title, i.year " +
            "having count(a.author) >10 " +
            "order by count(a.author) desc limit 1;";

    public String M5 =

            "SELECT editor, amount FROM " +
            "(SELECT e.editor AS editor, COUNT(*) AS amount " +
                "FROM editor e " +
                "INNER JOIN conference c ON e.proceedings_key = c.proceedings_key " +
                "WHERE c.title = \"' PODS '\" " +
                "GROUP BY e.editor) t " +
            "WHERE amount > 2 " +
            "ORDER BY amount DESC;";

    public String M6 =

            "select author,count(distinct conference.title) " +
            "from (select * " +
                "from author " +
                "where author.author = " +
                "(select author " +
                    "from(select author,max(publishcount) " +
                    "from (select author, count(author) as publishcount " +
                    "from author " +
                    "group by author " +
                    "order by count(author.author) desc) as maxAuthorPublishCount) as maxAuthor)) as maxAuthorAndKeyTable " +
            "inner join conference " +
            "on maxAuthorAndKeyTable.publication_key = conference.proceedings_key;";

}