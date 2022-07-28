const server = require("./../config/config.json")

describe("POST cart", () => {
    it("Add product to cart", () => {
        cy.request("POST", server.serverURL + "cart", {
            cart: Cypress.env("cartId"),
            amount: "2",
            product: Cypress.env("productId")
        })
            .then((response) => {
                expect(response.status).to.eq(200)
                expect(response.body).to.have.property("cartProductId")

                Cypress.env("cartProductId", response.body.cartProductId)
            })
    })
})

describe("GET cart", () => {
    it("Get cart by user ID", () => {

    })
})