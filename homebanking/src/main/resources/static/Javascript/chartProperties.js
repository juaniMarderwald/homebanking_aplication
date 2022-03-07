
Vue.component('chart', {
    extends: VueChartJs.Line,
    data() {
        return {
            etiquetas: [01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31],
            data:"",
            bgColors: '#0530F0'
        }
    },
    mounted() {
        axios.get('api/dailyBalance')
                .then(response=> {
                  this.data=response.data;
                  console.log(this.data);
                  this.renderChart({
                    labels: this.etiquetas,
                    datasets: [
                        {
                            label: 'Balance Mensual en $',
                            backgroundColor: this.bgColors,
                            data: this.data,
                        }
                    ]
                }, { responsive: true, maintainAspectRatio: false })
                })
                .catch(function (error) {
                    // handle error
                    console.log(error);
                })
                .then(function () {
                    // always executed
                });       
        
    },
    methods: {     
       
    }
})


var vm = new Vue({
    el: '#newChart',
    data: {     
        message: 'Balance de cuenta diario - Mes actual',      
    },
})