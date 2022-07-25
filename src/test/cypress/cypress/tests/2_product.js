const server = require("./../config/config.json")
const products = require("./../fixtures/product.json")

describe("POST product", () => {
    it("Add product", () => {
        cy.request("POST", server.serverURL + "products", {
            name: products[0].name,
            description: products[0].description,
            price: products[0].price,
            category: Cypress.env("cartId"),
            available: products[0].available
        })
            .then((response) => {
                expect(response.status).to.eq(200)
                expect(response.body).to.have.property("productId")

                Cypress.env("productId", response.body.productId)
            })
    })
})
