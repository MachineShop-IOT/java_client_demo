<html>
    <head></head>
    <body>
        <h1>Data Sources</h1>
        <br/>
        <a href="/data_sources/new">+ New Data Source</a>
        <br/>
        <ol>
        <#list data_sources as ds>
            <li>
                ${ds.name}
                <br/>
                <a href="/data_sources/${ds._id}/reports/new">Create Report</a>
            </li>
        </#list>
        </ol>
        
    </body>
</html>