const calcPension = function ({
    age,
    retirementAge,
    contributoryBaseWage,
    averageSocialWage,
    averageSocialWageGrowth,
    enterpriseRate,
    individualRate,
    monthlyContribution,
    balance }) {

    age = parseInt(age)
    retirementAge = parseInt(retirementAge)
    contributoryBaseWage = parseFloat(contributoryBaseWage)
    averageSocialWage = parseInt(averageSocialWage)
    averageSocialWageGrowth = parseFloat(averageSocialWageGrowth)
    enterpriseRate = parseFloat(enterpriseRate)
    individualRate = parseFloat(individualRate)
    monthlyContribution = parseInt(monthlyContribution)
    balance = parseInt(balance) || 0

    // 退休后每个月个人账户领到的钱 = 个人账户退休时余额 / 计发月份
    let years = retirementAge - age + 1
    let personnal_money = {
        
    }
    for (let index = 0; index < years; index++) {
       
        
    }
    contributoryBaseWage * 0.08 * 12 
    let rIndiviMoney = (balance + (contributoryBaseWage * 0.08 * years)) / (MonthSheat[retirementAge])

    this.formData.monthlyContribution = contributoryBaseWage * 0.24
    /**
     * 统筹养老金 
     */

    // 公司缴纳钱数
    // let enter = contributoryBaseWage  * 0.16 * years

    // 所在地计发基数 
    let locationBaseFigure = averageSocialWage
    // 缴费工资指数
    let averContributoryWageIndex = contributoryBaseWage / locationBaseFigure
    // 退休后每个月统筹账户领到的钱
    let rWholeMoney = (locationBaseFigure * (1 + averContributoryWageIndex)) / 2 * years * 0.01

    console.log(rIndiviMoney, rWholeMoney);

    return {
        pensionMoney: parseInt(rIndiviMoney + rWholeMoney)
    }
}

export {
    calcPension
}