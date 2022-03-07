package com.mindHub.homebanking.dtos;

public class LoanApplicationDTO {
//d del préstamo, monto, cuotas y número de cuenta de destino

    long id;
    String monto;
    int cuotas;
    String cuentaDestino;
    String loanName;

    //{id:selectLoan[0].id, monto:this.monto, cuotas:this.cuotasASolicitar,cuentaDestino:this.cuentaDestino }

    public LoanApplicationDTO(Long id,String name, String monto, int cuotas, String cuentaDestino ){
        this.id=id;
        this.loanName=name;
        this.monto=monto;
        this.cuotas=cuotas;
        this.cuentaDestino=cuentaDestino;

    }

    public long getId() {
        return id;
    }

    public String getMonto() {
        return monto;
    }

    public int getCuotas() {
        return cuotas;
    }

    public String getLoanName(){
        return this.loanName;
    }

    public String getCuentaDestino() {
        return cuentaDestino;
    }

    public void setLoanId(Long loanId) {
        this.id = loanId;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    public void setCuentaDestino(String cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public void setLoanName(String name){
        this.loanName=name;
    }
}
