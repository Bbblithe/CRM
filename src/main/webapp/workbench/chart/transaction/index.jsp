
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    /*
        需求：
            根据交易表中的不同阶段的数量进行一个统计，最终形成一个漏斗图（倒三角形）

            例如：
                01资质审查  10条
                02需求分析  85条
                。。。
                07成交    100条
            sql :
                select
                    stage count(*)
                from
                    crm.tbl_tran
                group by
                    stage
     */
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8">
    <title>echarts图表</title>
    <script src="ECharts/echarts.min.js"></script>
    <script src="jquery/jquery-1.11.1-min.js"></script>
    <script>
        $(function (){
            // 页面加载完毕之后，绘制统计图表
            getCharts();
        })

        function getCharts(){
            $.ajax({
                url:"workbench/transaction/getCharts.do",
                type:"get",
                dataType:"json",
                success:function (data){
                    // 基于准备好的dom，初始化echarts实例
                    // 指定图表的配置项和数据
                    let option = {
                        title: {
                            text: '交易漏斗图',
                            subtext: '统计交易阶段数量的漏斗图'
                        },

                        series: [
                            {
                                name:'交易漏斗图',
                                type:'funnel',
                                left: '10%',
                                top: 60,
                                //x2: 80,
                                bottom: 60,
                                width: '80%',
                                // height: {totalHeight} - y - y2,
                                min: 0,
                                max: data.total,
                                minSize: '0%',
                                maxSize: '100%',
                                sort: 'descending',
                                gap: 2,
                                label: {
                                    show: true,
                                    position: 'inside'
                                },
                                labelLine: {
                                    length: 10,
                                    lineStyle: {
                                        width: 1,
                                        type: 'solid'
                                    }
                                },
                                itemStyle: {
                                    borderColor: '#fff',
                                    borderWidth: 1
                                },
                                emphasis: {
                                    label: {
                                        fontSize: 20
                                    }
                                },
                                data: data.dataList
                            }
                        ]
                    };

                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);
                }
            })
        }
    </script>
</head>
<body>
    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div id="main" style="width: 600px;height:400px;"></div>
<script>
    let myChart = echarts.init(document.getElementById('main'));
</script>
</body>
</html>
