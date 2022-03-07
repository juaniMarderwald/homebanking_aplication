let newCards = new Vue({
    el: '#newCards',
    data: {
        color:"",
        tipo:"",
    },

    methods: {
        crearTarjeta() {
            console.log(this.color);
            if(this.color=="" || this.tipo==""){                                                
                Swal.fire({
                    icon: 'error',
                    text: 'Faltan seleccionar alguna característica!',
                    showConfirmButton: false
                });
            }else{
                axios.post('/api/clients/current/cards', `color=${this.color}&tipo=${this.tipo}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                    .then(response => {
                        console.log('registered');
                        Swal.fire({
                            text: 'Tarjeta creada con exito',
                            icon: 'success',
                            confirmButtonText: 'Ok',
                        }).then(() => {
                            window.location.href = "/cards.html";
                        })
                    })
                    .catch(function (error) {
                        if (error.response) {
                            console.log("ENTRA AL ERROR");
                            // Con el catch atajo el error del response, eso quiere decir que el email o la contraseña son invalidas, o no encontró el usuario o la contraseña está mal, y lo muestro por pantalla
                            Swal.fire({
                                icon: 'error',
                                text: 'No se puede crear mas tarjetas, llegó al máximo permitido!',
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
    }
});