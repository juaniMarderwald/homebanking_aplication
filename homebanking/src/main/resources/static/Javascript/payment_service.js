let paymentService = new Vue({
    el: '#paymentService',
    data: {
        monto: "",
        descripcion: "",
        cuentas: [],
        cuentaAsociada: "",
        cardHolder: "",
        cardNumber: "",
        expireMonth: "",
        expireYear: "",
        cvv: "",
    },
    created() {
        this.loadData();
        const valores = window.location.search;

        //Creamos la instancia
        const urlParams = new URLSearchParams(valores);

        //Accedemos al id
        this.monto = urlParams.get('monto');
        this.descripcion = urlParams.get('descripcion');
        console.log(this.monto);

    },
    methods: {
        loadData() {
            axios.get('api/clients/current')
                .then(function (response) {
                    paymentService.cuentas = response.data.accounts;
                })
                .catch(function (error) {
                    // handle error
                    console.log(error);
                })
                .then(function () {
                    // always executed
                });
        },

        makePayment() {
            axios.post('/api/payment', `number=${this.cardNumber}&cardHolder=${this.cardHolder}&cvv=${this.cvv}&expiredMonth=${this.expireMonth}&expiredYear=${this.expireYear}&accountNumber=${this.cuentaAsociada}&montoADebitar=${this.monto}&descripcionPago=${this.descripcion}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    Swal.fire({
                        icon: 'success',
                        text: 'Pago realizado con exito',
                        showConfirmButton: true
                    })
                    window.location.href = "/payment.html";                   
                })
                .catch(function (error) {
                    if (error.response) {
                        console.log("ENTRA AL ERROR");
                        // Con el catch atajo el error del response
                        Swal.fire({
                            icon: 'error',
                            text: error.response.data,
                            showConfirmButton: false
                        });
                        console.log(error.response.data);
                        console.log(error.response.status);
                        console.log(error.response.headers);
                    } else if (error.request) {
                        console.log(error.request);
                    } else {
                        // Something happened in setting up the request that triggered an Error
                        console.log('Error', error.message);
                    }
                    console.log(error.config);
                });

        }

    }

});