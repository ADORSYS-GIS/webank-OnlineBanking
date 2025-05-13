import { Command as $Command } from "@smithy/smithy-client";
import { MetadataBearer as __MetadataBearer } from "@smithy/types";
import {
  CloudWatchClientResolvedConfig,
  ServiceInputTypes,
  ServiceOutputTypes,
} from "../CloudWatchClient";
import {
  GetInsightRuleReportInput,
  GetInsightRuleReportOutput,
} from "../models/models_0";
export { __MetadataBearer };
export { $Command };
export interface GetInsightRuleReportCommandInput
  extends GetInsightRuleReportInput {}
export interface GetInsightRuleReportCommandOutput
  extends GetInsightRuleReportOutput,
    __MetadataBearer {}
declare const GetInsightRuleReportCommand_base: {
  new (
    input: GetInsightRuleReportCommandInput
  ): import("@smithy/smithy-client").CommandImpl<
    GetInsightRuleReportCommandInput,
    GetInsightRuleReportCommandOutput,
    CloudWatchClientResolvedConfig,
    ServiceInputTypes,
    ServiceOutputTypes
  >;
  new (
    input: GetInsightRuleReportCommandInput
  ): import("@smithy/smithy-client").CommandImpl<
    GetInsightRuleReportCommandInput,
    GetInsightRuleReportCommandOutput,
    CloudWatchClientResolvedConfig,
    ServiceInputTypes,
    ServiceOutputTypes
  >;
  getEndpointParameterInstructions(): import("@smithy/middleware-endpoint").EndpointParameterInstructions;
};
export declare class GetInsightRuleReportCommand extends GetInsightRuleReportCommand_base {
  protected static __types: {
    api: {
      input: GetInsightRuleReportInput;
      output: GetInsightRuleReportOutput;
    };
    sdk: {
      input: GetInsightRuleReportCommandInput;
      output: GetInsightRuleReportCommandOutput;
    };
  };
}
