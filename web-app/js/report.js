
var Report = {
    openedNotOpened: function(container, data) {
        new Highcharts.Chart({
            chart: {
                renderTo: container,
                margin: [0, 20, 60, 30]
            },
            colors: ['#0D52D1', '#666666'],
            legend: {
                enabled: false
            },
            plotOptions: {
                pie: {
                    cursor: 'pointer'
                }
            },
            tooltip: {
                borderWidth: 1,
                borderRadius: 3,
                shadow: false,
                formatter: function() {
                    return '<b>'+ this.point.name +'</b>: '+ this.y;
                }
            },
            series: [{
                type: 'pie',
                data: data
            }]
        });
    },

    opensClicks: function(container, categories, data) {
        new Highcharts.Chart({
            chart: {
                renderTo: container,
                margin: [10, 10, 60, 30],
                defaultSeriesType: 'line',
                alignTicks: false
            },
            colors: ['#0077CC', '#259E01'],
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer'
                }
            },
            tooltip: {
                borderWidth: 1,
                borderRadius: 3,
                shadow: false,
                formatter: function() {
                    if (this.x != '_') {
                        return '<i>' + this.x + '</i><br/>' +
                            '<b>'+ this.series.name +'</b> '+this.y;
                    }
                    else {
                        return '<b> No '+ this.series.name +'</b>';
                    }
                }
            },
            xAxis: {
                showLastLabel: true,
                startOnTick: true,
                showFirstLabel: false,
                tickColor: '#efefef',
                tickPosition: 'inside',
                allowDecimals: false,
                categories: categories,
                gridLineWidth: 1,
                gridLineColor: '#efefef',
                lineColor: '#afafaf',
                lineWidth: 1,
                minorGridLineWidth: 1,
                minorTickInterval: 'auto',
                minPadding: 0
            },
            yAxis: {
                tickColor: '#efefef',
                tickPosition: 'inside',
                min: 0,
                minPadding: 0,
                startOnTick: true,
                endOnTick: true,
                allowDecimals: false,
                lineColor: '#afafaf',
                lineWidth: 1,
                gridLineWidth: 1,
                gridLineColor: '#eaeaea',
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            symbols: ['circle', 'circle'],
            series: data
        });
    }
};
