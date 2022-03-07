let pdfGenerator = new Vue({
    el: '#pdfGenerator',
    data: {
        cliente: "",
        fechaDesde: "",
        fechaHasta: ""
        
    },
    created() {
        this.loadData();
    },
    methods: {
        loadData() {
            axios.get('api/clients/current')
                .then(function (response) {
                    console.log(response);
                    pdfGenerator.cliente = response.data.firstName+"_"+response.data.lastName;
                    console.log(this.cliente);
                })
                .catch(function (error) {
                    // handle error
                    console.log(error);
                })
                .then(function () {
                    // always executed
                });
        },
        downloadBalance() {

            if(pdfGenerator.fechaDesde==""&&pdfGenerator.fechaDesde==""){
                Swal.fire({
                    icon: 'error',
                    title: 'Fechas no especificadas!',
                    text:"Por favor seleccione fecha de inicio y fin deseadas para poder filtrar",
                    showConfirmButton: true
                })
            }else{
                const valores = window.location.search;

                //Creamos la instancia
                const urlParams = new URLSearchParams(valores);
    
                //Accedemos al id
                var id = urlParams.get('id');
    
    
                console.log("ENTRO AL METODO");
                axios({
                    url: `/api/pdf/generate/${id}`,
                    method: 'GET',
                    responseType: 'blob',
                    params: {
                        fechaDesde: pdfGenerator.fechaDesde,
                        fechaHasta: pdfGenerator.fechaHasta
                    }
                })
                .then((response) => {
                    const url = window.URL.createObjectURL(new Blob([response.data]));
                    const link = document.createElement('a');
                    console.log(response);
                    link.href = url;                    
                    link.setAttribute('download', 'FREEBANKING_balance_' + pdfGenerator.cliente + '_'+ pdfGenerator.fechaDesde + '_a_'+ pdfGenerator.fechaHasta+ '.pdf');
                    document.body.appendChild(link);
                    link.click();
                    this.reiniciarValores();
                })
                .catch(function (error) {
                    // handle error
                    console.log(error);
                })     
            }
           
        },
        reiniciarValores(){
            pdfGenerator.fechaDesde="";
            pdfGenerator.fechaHasta= "";
        }
    }

});