<#setting locale="en_EN">
<#list customers as customer>

Dear ${customer.name},

we would like to inform you that your debt ${customer.amount?string.currency} is
due ${customer.dueDate?date}.

Best regards,

Your bank

</#list>
